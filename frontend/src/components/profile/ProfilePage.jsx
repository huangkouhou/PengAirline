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
        <div className="profile-page">
            <div className="profile-container">
                <ErrorDisplay />

                <div className="profile-header">
                    <h2>My Account</h2>
                    <div className="welcome-message">
                        Welcome back, <strong>{user.name}</strong>
                    </div>
                </div>

                <div className="profile-tap">
                    <button
                        className={activeTab === "profile" ? "active" : ""}
                        onClick={() => setActiveTab("profile")}
                    >
                        Profile
                    </button>

                    <button
                        className={activeTab === "bookings" ? "active" : ""}
                        onClick={() => setActiveTab("bookings")}
                    >
                        My Bookings
                    </button>
                </div>

                <div className="profile-content">
                    {activeTab === "profile" ? (

                        <div className="profile-info">
                            <div className="info-card">
                                <h3>Personal Information</h3>
                                <div className="info-row">
                                    <span className="label">Name:</span>
                                    <span className="value">{user.name}</span>
                                </div>
                                <div className="info-row">
                                    <span className="label">Email:</span>
                                    <span className="value">{user.email}</span>
                                </div>
                                <div className="info-row">
                                    <span className="label">Phone:</span>
                                    <span className="value">{user.phoneNumber}</span>
                                </div>
                                <div className="info-row">
                                    <span className="label">Account Status:</span>
                                    <span className="value">
                                        {user.active ? "Active" : "Inactive"}
                                    </span>
                                </div>
                            </div>

                            <div className="info-card">
                                <h3>Account Security</h3>
                                <div className="info-row">
                                    <span className="label">Email Verified:</span>
                                    <span className="value">
                                        {user.emailVerified ? "Yes" : "No"}
                                    </span>
                                </div>
                                <div className="info-row">
                                    <span className="label">Login Method:</span>
                                    <span className="value">
                                        {user.provider === "LOCAL" ? "Email/Password" : user.provider}
                                    </span>
                                </div>
                                <Link to="/update-profile" className="update-profile">
                                    Update Profile
                                </Link>

                            </div>
                        </div>
                        
                    ) : (
                        <div className="bookings-list">

                        </div>
                    )}


                </div>

            </div>

        </div>
    
    
    
    
    );

    }
}

export default ProfilePage;
