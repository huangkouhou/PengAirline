    import { useState, useEffect } from "react";
    import { useNavigate, Link, useLocation } from "react-router-dom";
    import ApiService from "../../services/ApiService";
    import { useMessage } from "../common/MessageDisplay";


    const FindFlightsPage = () => {

        const { ErrorDisplay, SuccessDisplay, showError } = useMessage();

        const [flights, setFlights] = useState();
        const [airports, setAirports] = useState();
        const [loading, setLoading] = useState();

        const location = useLocation();
        const navigate = useNavigate();

        const [searchParams, setSearchParams] = useState({
            departureIataCode: "",
            arrivalIataCode: "",
            departureDate: ""
        });


        // 组件挂载时拉取机场列表Fetch all airport on component mount
        useEffect(() => {
            const fetchAirports = async () => {
                try {
                    const response = await ApiService.getAllAirports();
                    setAirports(response.data || []);
                } catch (error) {
                    showError("Failed to load airports");
                }
            };
            fetchAirports();
        }, []);//只在组件第一次渲染（mount）时执行一次


        //初始化时从 URL 里读查询参数
        useEffect(() => {
            const params = new URLSearchParams(location.search);

            const initialParams = {
                departureIataCode: params.get("departureIataCode") || "",
                arrivalIataCode: params.get("arrivalIataCode") || "",
                departureDate: params.get("departureDate") || ""
            };

            // 初始化时，只同步一次，不要每次路由变更都写回 URL
            setSearchParams(initialParams);

            if (initialParams.departureIataCode || initialParams.arrivalIataCode){
                fetchFlights(initialParams)
            } else {
                setLoading(false)
            }
        }, []);   // ← 改成空数组，只执行一次


        //根据参数去后台查机票
        const fetchFlights = async(initialParams) => {
            try {
                setLoading(true)
                //把前端的查询条件（出发、到达、日期）传给后端 API
                const response = await ApiService.searchFlights(
                    initialParams.departureIataCode,
                    initialParams.arrivalIataCode,
                    initialParams.departureDate
                )
                //保存后端返回的航班列表，后面 UI 渲染时就用这个列表
                setFlights(response.data);
            } catch(error){
                showError(error.response?.data?.message || "Failed to fetch flights");
            } finally {
                setLoading(false)
            }
        }


        //点击“搜索航班”按钮时的处理逻辑
        const handleSearch = (e) => {
            e.preventDefault();
            if (!searchParams.departureIataCode || !searchParams.arrivalIataCode){
                showError("Please select both departure and arrival airports");
                return;
            }

            //构造 URL 查询参数
            const query = new URLSearchParams();
            query.append("departureIataCode", searchParams.departureIataCode);
            query.append("arrivalIataCode", searchParams.arrivalIataCode);
            query.append("departureDate", searchParams.departureDate);

            navigate(`/flights?${query.toString()}`);//跳转到带 query 的 URL

        };

        const handleSwapAirports = () => {
            setSearchParams({
                ...searchParams,
                departureIataCode: searchParams.arrivalIataCode,
                arrivalIataCode: searchParams.departureIataCode
            });
        };

        //把时间字符串转成“小时:分钟 AM/PM”
        const formatAirportOption = (airport) => {
            return `${airport.iataCode} (${airport.city}) - ${airport.name}`;
        };

        const formatTime = (dateTime) => {
            return new Date(dateTime).toLocaleTimeString([], {
                hour: '2-digit',
                minute: '2-digit',
                hour12: true
            });
        };

        //把日期格式化成“星期 月 日”
        const formatDate = (dateTime) => {
            return new Date(dateTime).toLocaleDateString([], {
                weekday: 'short',
                month: 'short',
                day: 'numeric'
            });
        };

        //计算飞行时长
        const calculateDuration = () => {
            const dep = new Date(departureTime);
            const arr = new Date(arrivalTime);
            const diff = arr - dep;

            const hours = Math.floor(diff / (1000 * 60 * 60));
            const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));

            return `${hours}h ${minutes}m`;
        };






    }