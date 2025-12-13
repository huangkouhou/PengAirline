import { useState, useEffect } from "react";
import { useParams, Link } from "react-router-dom";
import ApiService from "../../services/ApiService";
import { useMessage } from "../common/MessageDisplay";

const BookingDetailsPage = () => {

    const { id } = useParams();
    const { ErrorDisplay, SuccessDisplay, showError, showSuccess } = useMessage();
    const [booking, setBooking] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchBookingDetails();
    }, [id]);

    const fetchBookingDetails = async() => {
        try {
            const response = await ApiService.getBookingById(id);
            setBooking(response.data);
        } catch (error) {
            showError(error.response?.data?.message || "Failed to fetch booking details");
        } finally {
            setLoading(false);
        }
    };

    const formatDate = (dateTime) => {
        return new Date(dateTime).toLocaleDateString([], {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    };

    const calculateTotalPrice = () => {

        if (!booking || !booking.flight) return 0;
        return booking.passengers.reduce((total, passenger) => {//累加器函数
            let price = booking.flight.basePrice;
            if (passenger.type === 'CHILD') {
                price *= 0.75; // 25% discount for children
            } else if (passenger.type === 'INFANT') {
                price *= 0.1; // 90% discount for infants
            }
            return total + price;
        }, 0);
    };

    if (loading) return <div className="booking-details-loading">Loading booking details...</div>;
    if (!booking) return <div className="booking-details-error">Booking not found</div>;

    return (
        <div></div>
    );




}