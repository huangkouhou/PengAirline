import { useState, useEffect } from "react";
import { useParams, useNavigate, Link } from "react-router-dom";
import ApiService from "../../services/ApiService";
import { useMessage } from "../common/MessageDisplay";

const AdminFlightDetailsPage = () => {

    const { id } = useParams();

    const { ErrorDisplay, SuccessDisplay, showError, showSuccess } = useMessage();
    const navigate = useNavigate();
    const [flight, setFlight] = useState(null);
    const [loading, setLoading] = useState(true);
    const [selectedStatus, setSelectedStatus] = useState("");
    const [bookings, setBookings] = useState([]);

    useEffect(() => {
        fetchFlightDetails();
    }, [id]);

    const fetchFlightDetails = async () => {
        try {
            const [flightRes, bookingsRes] = await Promise.all([
                ApiService.getFlightById(id),
                ApiService.getAllBookings()
            ]);
            setFlight(flightRes.data);
            setSelectedStatus(flightRes.data.status);
            //Filter bookings for this flight
            setBookings(bookingsRes.data.filter(b => b.flight?.id === parseInt(id)));
        } catch (error) {
            showError(error.response?.data?.message || "Failed to fetch flight details");
        } finally {
            setLoading(false);
        }
    };


    const handleStatusChanged = async () => {
        try {
            const resp = await ApiService.updateBookingStatus(id, selectedStatus);

            if (resp.statusCode === 200) {
                showSuccess("Booking status updated successfully!");
                fetchBookingDetails();
            }
        } catch (error) {
            showError(error.response?.data?.message || "Failed to update booking status");
        }
    };


    const handleStatusChange = async () => {
        try {
            const resp = await ApiService.updateFlight({
                id: flight.id,
                status: selectedStatus
            });

            if (resp.statusCode === 200) {
                showSuccess("Flight status updated successfully!")
                fetchFlightDetails();
            }
        } catch (error) {
            showError(error.response?.data?.message || "Failed to update flight status");
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



    if (loading) return <div className="admin-flight-loading">Loading flight details...</div>;
    if (!flight) return <div className="admin-flight-error">Flight not found</div>;
    
    
    return (
        <div></div>
    );
    


}

export default AdminFlightDetailsPage;