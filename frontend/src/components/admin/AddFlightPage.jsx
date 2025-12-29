import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import ApiService from "../../services/ApiService";
import { useMessage } from "../common/MessageDisplay";

const AddFlightPage = () => {

    const { ErrorDisplay, SuccessDisplay, showError, showSuccess } = useMessage();
    const navigate = useNavigate();
    const [flight, setFlight] = useState({
        flightNumber: "",
        departureAirportIataCode: "",
        arrivalAirportIataCode: "",
        departureTime: "",
        arrivalTime: "",
        basePrice: "",
        pilotId: ""
    });

    const [airports, setAirports] = useState([]);
    const [pilots, setPilots] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async() => {
        try {
            //同时（并行）发起了“获取机场”和“获取飞行员”两个请求
            const [airportsRes, pilotsRes] = await Promise.all([
                ApiService.getAllAirports(),
                ApiService.getAllPilots()
            ]);

            //[airportsRes, pilotsRes] 直接把两个请求返回的结果分别拿出来了
            setAirports(airportsRes.data || []);

            //Transform pilots data to simpler format for the dropdown
            //前端只需要 id 和 name
            const formattedPilots = (pilotsRes.data || []).map(pilot => ({
                id: pilot.id,
                name: pilot.name
            }));
            setPilots(formattedPilots);

        } catch(error) {
            showError(error.response?.data?.message || "Failed to fetch data");
        } finally {
            setLoading(false);
        }
    }

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFlight(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async(e) => {
        e.preventDefault();
        try {
            //Prepare the flight data in the required format
            const flightData = {
                flightNumber: flight.flightNumber,
                departureAirportIataCode: flight.departureAirportIataCode,
                arrivalAirportIataCode: flight.arrivalAirportIataCode,
                departureTime: flight.departureTime,
                arrivalTime: flight.arrivalTime,
                basePrice: parseFloat(flight.basePrice),
                pilotId: parseInt(flight.pilotId)
            };

            await ApiService.createFlight(flightData);
            showSuccess("Flight created successfully!");
            navigate("/admin");
        } catch (error) {
            showError(error.response?.data?.message || "Failed to create flight");
        }

    }

    if (loading) return <div className="flight-form-loading">Loading...</div>;

    return (
        <div></div>
    );




}

export default AddFlightPage;