import { useNavigate, Link } from "react-router-dom";
import { useMessage } from "../common/MessageDisplay";
import { useState } from "react";
import ApiService from "../../services/ApiService";

const registerPage = () => {

    const { ErrorDisplay, SuccessDisplay, showError, showSuccess } = useMessage();
    const navigate = useNavigate();

    //声明一个 formData 状态对象，来保存所有输入框的内容，初始值为空
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        password:'',
        phoneNumber:'',
        confirmPassword: ''
    });

    //任何 input 改变时更新对应字段（...formData 会把旧的数据复制出来，[e.target.name]: e.target.value 会覆盖对应字段）
    const handleChange = (e) => {
        setFormData({...formData, [e.target.name]:e.target.value});
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!formData.name || !formData.email || !formData.password || !formData.phoneNumber | !formData.confirmPassword){
            showError("All fields are required")
            return;
        }

        if (formData.name !== formData.confirmPassword){
            showError("Password do not match")
            return;
        }

        const registrationData = {
            name: formData.name,
            email: formData.email,
            password: formData.password,
            phoneNumber: formData.phoneNumber,
        }

        try {
            const response = await ApiService.registerUser(registrationData);
            if (response.statusCode === 200){
                showSuccess("User Successfully Registered")
                navigate("/login")
            } else {
                showError(response.message);
            }
        } catch (error){
            showError(error.response?.data?.message || error.message);
        }

    };

    return (
        <></>
    );






}

export default registerPage;