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

    const addPassenger = () => {
        setPassengers([
            ...passengers,
            {
                firstName: "",
                lastName: "",
                passportNumber: "",
                type: "ADULT",
                seatNumber: "",
                specialRequest: ""
            }
        ]);
    };

    //删除指定位置的乘客，但保留至少一位乘客
    const removePassenger = (index) => {
        if (passengers.length <= 1) return;
        const updatedPassengers = [...passengers];//把当前的 passengers 数组复制一份，起名叫 updatedPassengers
        updatedPassengers.splice(index, 1);//在副本数组中，从 index 这个位置开始，删除 1 个元素。（比如删第2个，index就是1）
        setPassengers(updatedPassengers);
    };

    const handlePassengerChange = (index, field, value) => {
        const updatedPassengers = [...passengers];
        updatedPassengers[index][field] = value;//updatedPassengers[index]：先找到 第 X 位乘客 的数据对象。updatedPassengers[index]：先找到 第 X 位乘客 的数据对象。= value：把它赋值为 Z。
        setPassengers(updatedPassengers);
    };

    const calculateTotalPrice = () => {
        if (!flight) return 0;
        return passengers.reduce((total, passenger) => {
            let price = flight.basePrice;
            if (passenger.type === "CHILD") {
                price *= 0.75; // 25% discount for children
            } else if (passenger.type === "INFANT") {
                price *= 0.1; // 90% discount for infants
            }
            return total + price;
        }, 0);
    }

    const handleSubmit = async(e) => {

        //Validate form
        for (let i = 0; i < passengers.length; i++) {
            const p = passengers[i];
            if (!p.firstName || !p.lastName || !p.passportNumber || !p.seatNumber) {
                showError(`Please fill all required fields for Passenger ${i + 1}`);
                return;
            }
        }

        try {
            const bookingData = {
                flightId: parseInt(flightId),
                passengers: passengers
            };

            const response = await ApiService.createBooking(bookingData);

            if (response.statusCode === 200){
                showSuccess("Flight Booking Successfully")
                navigate(`/profile`);
            }

        } catch (error) {
            showError(error.response?.data?.message || "Failed to book a flight");
        }

    };

    if (loading) return <div className="booking-loading">Loading Flight details</div>
    if (!flight) return <div className="booking-error">Flight Not Found</div>

    return (
        <div></div>
    );




}
export default BookingPage;