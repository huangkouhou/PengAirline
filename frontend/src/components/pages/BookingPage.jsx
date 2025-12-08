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

        if (state?.flight) {
            console.log("Flight Found from State")
            setFlight(state.flight)
            setLoading(false)
            generateAvailableSeats(state.flight)
        } else {
            console.log("Flight Not Found from State")
            fetchFlightDetails();
        }
    });

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