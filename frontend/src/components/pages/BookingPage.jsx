import { useState, useEffect } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import ApiService from "../../services/ApiService";
import { useMessage } from "../common/MessageDisplay";

const BookingPage = () => {

    const { id: flightId } = useParams();
    const { state } = useLocation();
    const { ErrorDisplay, SuccessDisplay, showError, showSuccess } = useMessage();
    const navigate = useNavigate();

    const [flight, setFlight] = useState(null);
    const [loading, setLoading] = useState(true);

    const [availableSeats, setAvailableSeats] = useState([]);

    //初始化乘客状态 (useState)
    const [passengers, setPassengers] = useState([
        {
            firstName: "",
            lastName: "",
            passportNumber: "",
            type: "ADULT",
            seatNumber: "",
            specialRequest: ""
        }
    ]);

    useEffect(() => {

        // 检查路由状态中是否已经包含了 flight 数据，数据预加载 / 状态传递” (Pre-loading / State Passing)把上一页的航班数据直接打包带了过来
        if (state?.flight) {
            console.log("Flight Found from State")
            setFlight(state.flight)
            setLoading(false)
            generateAvailableSeats(state.flight)
        } else {
            console.log("Flight Not Found from State")
            fetchFlightDetails();
        }
    }, []);

    //获取数据函数
    const fetchFlightDetails = async () => {
        try {
            setLoading(true)
            const response = await ApiService.getFlightById(flightId);
            setFlight(response.data)
            generateAvailableSeats(response.data)

        } catch (error) {
            showError(error.response?.data?.message || "Failed to fetch flight details");

        } finally {
            setLoading(false)
        }
    }


    //生成座位算法
    const generateAvailableSeats = (flightData) => {
        // Generate available seats (simple implementation - in real app you'd check booked seats)
        const seats = [];
        for (let i = 1; i <= 20; i++) {
            for (let j = 0; j < 4; j++) {
                const seatLetter = String.fromCharCode(65 + j); // A-F
                seats.push(`${i}${seatLetter}`);
            }
        }
        setAvailableSeats(seats);
    };



}
export default BookingPage;