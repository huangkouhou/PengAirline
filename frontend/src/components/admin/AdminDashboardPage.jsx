import { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import ApiService from "../../services/ApiService";
import { useMessage } from "../common/MessageDisplay";

const AdminDashboardPage = () => {

    const { ErrorDisplay, SuccessDisplay, showError, showSuccess } = useEffect();
    const navigate = useNavigate();
    const [activeTab, setActiveTab] = useState();
    const [bookings, setBookings] = useState();
    const [flights, setFlights] = useState();
    const [airports, setAirports] = useState();
    const [loading, setLoading] = useState();

    useEffect(() => {
        fetchAllData();
    });

    const fetchAllData = async() => {
        try{
            const [bookingsRes, flightsRes, airportsRes] = await Promise.all([

                ApiService.getAllBookings(),
                ApiService.getAllFlights(),
                ApiService.getAllAirports(),
            ]);

            setBookings(bookingsRes.data || []);
            setFlights(flightsRes.data || []);
            setAirports(airportsRes.data || []);

        } catch (error) {
            showError(error.response?.data?.message || "Failed to fetch data");
        } finally {
            setLoading(false);
        }

    }

    const formatDate = (dateTime) => {
        return new Date(dateTime).toLocaleDateString([], {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    };

    if (loading) return <div className="admin-loading">Loading dashboard...</div>;

    return (
        <div></div>
    );


}

export default AdminDashboardPage;