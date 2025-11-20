import { useState, useEffect } from "react";
import { useNavigate, Link } from 'react-router-dom';
import ApiService from "../../services/ApiService";
import { useMessage } from "../common/MessageDisplay";

const HomePage = () => {

    const { ErrorDisplay, SuccessDisplay, showError } = useNavigate();
    const navigate = useNavigate();
    const [airports, setAirports] = useState([]);

    const [searchData, setSearchData] = useState({
        departureIataCode: "",
        arrivalIataCode: "",
        departDate: ""
    });

    const popularDestinations = [
        { id: 1, city: "New York", country: "USA", price: "$450", image: "usa.jpg" },
        { id: 2, city: "London", country: "UK", price: "$380", image: "uk.jpg" },
        { id: 3, city: "Dubai", country: "UAE", price: "$520", image: "uae.webp" },
        { id: 4, city: "Tokyo", country: "Japan", price: "$680", image: "japan.webp" }
    ];

    //Fetch all airports on component mount
    useEffect(() => {
        const fetchAirports = async () => {
            try {
                const response = await ApiService.getAllAirports();//调用 axios 去请求后端 /airports API
                setAirports(response.data || []);//把机场列表放进 React 的 state，如果null、undefined 或 false，就使用空数组 [] 作为默认值
            } catch (error) {
                showError("Failed to load airports");
            }
        };
        fetchAirports();//最后立即调用它
    },[])//[] 代表只在组件第一次渲染时执行一次


    const handleSearch = async(e) => {
        e.preventDefault();

        if (!searchData.departureIataCode || !searchData.arrivalIataCode){
            showError("Please select departure and arrival airports");
            return;
        }
        navigate(`flights?departureIataCode=${searchData.departureIataCode}&arrivalIataCode=${searchData.arrivalIataCode}&departDate=${searchData.departDate}`)
    };

    //交换出发机场和到达机场
    const handleSwapAirports = () => {
        setSearchData({
            ...searchData,//展开原本的 searchData（保留其他字段），再交换两个值
            departureIataCode: searchData.arrivalIataCode,//重新覆盖
            arrivalIataCode: searchData.departureIataCode //重新覆盖
        });
    };

    //格式化机场选项（字符串）把某个机场对象格式化成下列格式：IATA(city)-Airport Name
    const formatAirportOption = (airport) => {
        return `${airport.iataCode}(${airport.city})-${airport.name}`;
    };






}