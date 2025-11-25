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






}