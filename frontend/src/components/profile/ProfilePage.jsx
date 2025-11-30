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

    


}