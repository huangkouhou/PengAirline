import { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import ApiService from "../../services/ApiService";
import { useMessage } from "../common/MessageDisplay";

const UpdateProfilePage = () => {

    const { ErrorDisplay, SuccessDisplay, showError, showSuccess } = useMessage();
    const [ loading, setLoading ] = useState(true);

    const navigate = useNavigate();

    const [user, setUser] = useState({
        name: "",
        phoneNumber: "",
        password: "",
        confirmPassword: ""
    });

    useEffect(() => {
        fetchUserProfile();
    }, []);

    const fetchUserProfile = async() => {
        try {
            const response = await ApiService.getAccountDetails();

            setUser(prev => ({
                ...prev,
                name: response.data.name,
                phoneNumber: response.data.phoneNumber || ""
            }));
        } catch (error) {
            showError(error.response?.data?.message || "Failed to fetch profile");
        } finally {
            setLoading(false);
        }
    }

}

export default UpdateProfilePage;
