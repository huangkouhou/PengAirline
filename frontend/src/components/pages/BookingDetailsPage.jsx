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
        <div className="booking-details-container">
            <div className="booking-details-card">
                <ErrorDisplay />
                <SuccessDisplay />
            </div>

            <h2 className="booking-details-title">Booking Details</h2>

            <div className="booking-details-summary">
                <div className="booking-details-flight-info">
                    <div className="booking-details-flight-number">
                        Flight: {booking.flight?.flightNumber || "N/A"}
                    </div>
                    <div className="booking-details-route">
                        <span className="booking-details-departure">
                            {booking.flight?.departureAirport?.iataCode} → {booking.flight?.arrivalAirport?.iataCode}
                        </span>
                        <span className="booking-details-date">
                            {formatDate(booking.flight?.departureTime)}
                        </span>
                    </div>
                </div>
                <div className="booking-details-price">
                    ${calculateTotalPrice().toFixed(2)}
                </div>
            </div>

            <div className="booking-details-info-section">
                <div className="booking-details-info-card">
                    <h3 className="booking-details-subtitle">Booking Information</h3>
                    <div className="booking-details-info-row">
                        <span className="booking-details-label">Reference Number:</span>
                        <span className="booking-details-value">{booking.bookingReference}</span>
                    </div>
                    <div className="booking-details-info-row">
                        <span className="booking-details-label">Booking Date:</span>
                        <span className="booking-details-value">{formatDate(booking.bookingDate)}</span>
                    </div>
                    <div className="booking-details-info-row">
                        <span className="booking-details-label">Status:</span>
                        <span className={`booking-details-value booking-details-status-${booking.status.toLowerCase()}`}>
                            {booking.status}
                        </span>
                    </div>
                </div>




            </div>


        </div>
    );




}