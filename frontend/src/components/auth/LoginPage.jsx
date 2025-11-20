import { useNavigate, Link } from "react-router-dom";
import { useMessage } from "../common/MessageDisplay";
import { useState } from "react";
import ApiService from "../../services/ApiService";

const LoginPage = () => {

    const { ErrorDisplay, SuccessDisplay, showError, showSuccess } = useMessage();
    const navigate = useNavigate();

    //声明一个 formData 状态对象，来保存所有输入框的内容，初始值为空
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        password:''
    });

    //任何 input 改变时更新对应字段（...formData 会把旧的数据复制出来，[e.target.name]: e.target.value 会覆盖对应字段）
    const handleChange = (e) => {
        setFormData({...formData, [e.target.name]:e.target.value});
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!formData.email || !formData.password){
            showError("Email & Password are required")
            return;
        }

        const loginData = {
            email: formData.email,
            password: formData.password
        };

        try {
            const response = await ApiService.loginUser(loginData);
            if (response.statusCode === 200){
                ApiService.saveRole(response.data.roles)
                ApiService.saveToken(response.data.token)
                navigate("/home")
            } else {
                showError(response.message);
            }
        } catch (error){//优先显示后端返回的 message，其次显示 JS 系统错误。
            showError(error.response?.data?.message || error.message);
        }

    };

    return (
        <div className="auth-page">
            <div className="auth-card">
                <ErrorDisplay/>
                <SuccessDisplay/>

                <div className="auth-header">
                    <h2>Welcome to Peng Airlines</h2>
                    <p>Sign in to book your next flight</p>
                </div>

                <form className="auth-form" onSubmit={handleSubmit}>

                    <div className="form-group">
                        <label htmlFor="">Email Address</label>
                        <input
                            type="email"
                            name="email"
                            id="email"
                            value={formData.email}
                            onChange={handleChange}
                            required
                            placeholder="Enter your email ..."
                        />
                    </div>


                    <div className="form-group">
                        <label htmlFor="">Password</label>
                        <input
                            type="password"
                            name="password"
                            id="password"
                            value={formData.password}
                            onChange={handleChange}
                            required
                            placeholder="Enter your phone password ..."
                        />
                    </div>

                    <button type="submit" className="auth-button">
                        Sign In
                    </button>

                    <div className="auth-footer">
                        <p>Don't have an account? <Link to="/register"> Register here </Link></p>
                    </div>


                </form>
            </div>
        </div>
    );






}

export default LoginPage;