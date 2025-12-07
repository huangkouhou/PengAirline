import { useState, useEffect, useCallback } from "react";//useCallback把原本每次渲染都会新建的函数 缓存下来，只有在依赖项变化时才重新生成。
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

    const fetchUserProfile = useCallback(async () => {
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
    }, [showError]);

    useEffect(() => {
        fetchUserProfile();
    }, [fetchUserProfile]);




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

        // 如果用户输入了密码，检查两者是否一致
        if (user.password && user.password !== user.confirmPassword) {
            showError("Passwords do not match!");
            return; // 阻止提交
        }


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

                // 更新成功后，清空密码框以确保安全
                setUser(prev => ({...prev, password: "", confirmPassword: ""}));

            }

        } catch (error) {
            showError(error.response?.data?.message || "Failed to update profile");

        }
    }


    if (loading) return <div className="update-profile-loading">Loading Profile</div>

    return (
        <div className="update-profile-container">
            <div className="update-profile-card">
                <ErrorDisplay/>
                <SuccessDisplay/>

                <h2 className="update-profile-title">Update Profile</h2>

                <form onSubmit={handleSubmit} className="update-profile-form">
                    <div className="update-profile-form-group">
                        <label htmlFor="name" className="update-profile-label">
                            Full Name
                        </label>
                        <input
                            type="text"
                            id="name"
                            name="name"
                            value={user.name}
                            onChange={handleChange}
                            className={`update-profile-input`}
                        />
                    </div>

                    <div className="update-profile-form-group">
                        <label htmlFor="phoneNumber" className="update-profile-label">
                            Phone Number
                        </label>
                        <input
                            type="tel"
                            id="phoneNumber"
                            name="phoneNumber"
                            value={user.phoneNumber}
                            onChange={handleChange}
                            className="update-profile-input"
                            placeholder="Optional"
                        />
                    </div>

                    <div className="update-profile-form-group">
                        <label htmlFor="password" className="update-profile-label">
                            New Password
                        </label>
                        <input
                            type="password"
                            id="password"
                            name="password"
                            value={user.password}
                            onChange={handleChange}
                            className="update-profile-input"
                            placeholder="Leave blank to keep current password"
                        />
              
                    </div>

                    <div className="update-profile-form-group">
                        <label htmlFor="confirmPassword" className="update-profile-label">
                            Confirm Password
                        </label>
                        <input
                            type="password"
                            id="confirmPassword"
                            name="confirmPassword"
                            value={user.confirmPassword}
                            onChange={handleChange}
                            className="update-profile-input"
                            placeholder="Confirm new password"
                        />
                
                    </div>

                    <div className="update-profile-actions">
                        <button type="submit" className="update-profile-submit">
                            Save Changes
                        </button>
                        <Link to="/profile" className="update-profile-cancel">
                            Cancel
                        </Link>
                    </div>
                </form>

            </div>

        </div>
    );


}

export default UpdateProfilePage;
