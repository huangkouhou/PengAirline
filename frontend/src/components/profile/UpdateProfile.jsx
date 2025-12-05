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


    const handleChange = (e) => {
        const { name, value } = e.target

        setUser(prev => (
            {
                ...prev, [name]: value
            }
        ))
    }

    const handleSubmit = async (e) => {

        e.preventDefault();

        console.log("handleSubmit called")


        try {
            const requestBody = {
                name: user.name,
                phoneNumber: user.phoneNumber || null,
                password: user.password || undefined,

            }

            const isToUpdate = window.confirm("Are you sure you want to update your account?");

            if (!isToUpdate) return;

            const resp = await ApiService.updateMyAccount(requestBody);

            if (resp.statusCode === 200) {
                showSuccess("Account updated successfully!");
                navigate("/profile");
            }

        } catch (error) {
            showError(error.response?.data?.message || "Failed to update profile");

        }
    }


    if (loading) return <div className="update-profile-loading">Loading Profile</div>

    return (
        <div></div>
    );


}

export default UpdateProfilePage;
