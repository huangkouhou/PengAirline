import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import ApiService from "../../services/ApiService";
import { useMessage } from "../common/MessageDisplay";

const ProfilePage = () => {

    const { ErrorDisplay, showError } = useMessage();

    const [user, setUser] = useState(null);
    const [bookings, setBookings] = useState([]);

    const [activeTab, setActiveTab] = useState(true);

    useEffect(() => {
        fetchUserProfile();
        fetchUserBookings();

    }, []);

    const fetchUserProfile = async() => {
        try{
            const response = await ApiService.getAccountDetails();
            setUser(response.data)
        }catch(error){
            showError(error.response?.data?.message || "Failed to fetch profile");
        }finally{
            setLoading(false)
        }
    }

    const fetchUserBookings = async() => {
        try{
            const response = await ApiService.getCurrentUserBookings();
            setBookings(response.data)
        }catch(error){
            showError(error.response?.data?.message || "Failed to fetch bookings");
    }

    const formatDate = (dateTime) => {
        return new Date(datetime).toLocaleDateString([], {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    }

    if (loading) return <div className="loading">Loading Profile...</div>
    if (!user) return <div className="error">User Not Found</div>

    return (
        <div></div>
    
    
    
    
    );

}

export default ProfilePage;
