import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import ApiService from '../../services/ApiService';
import { useMessage } from "../common/MessageDisplay";

const SpecialRegistration = () => {

    //use the error hook
    const { ErrorDisplay, SuccessDisplay, showError, showSuccess } = useMessage();
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        name: '',
        email: '',
        password: '',
        phoneNumber: '',
        roles: []
    });

    const availableRoles = [
        { value: 'ADMIN', label: 'Admin' },
        { value: 'PILOT', label: 'Pilot' }
    ];


    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleRoleChange = (roleValue) => {
        setFormData(prev => {
            if (prev.roles.includes(roleValue)) {
                return { ...prev, roles: prev.roles.filter(r => r !== roleValue) };
            } else {
                return { ...prev, roles: [...prev.roles, roleValue] };
            }
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!formData.name || !formData.email || !formData.password ||
            !formData.phoneNumber) {
            showError('All fields except roles are required');
            return;
        }

        if (formData.roles.length === 0) {
            showError('Please select at least one role');
            return;
        }

        try {
            const response = await ApiService.registerUser(formData);
            if (response.statusCode === 200) {
                setFormData({
                    name: '', email: '', password: '',
                    phoneNumber: '', roles: []
                });
                navigate('/admin'); // Redirect to admin page
            } else {
                showError(response.message);
            }
        } catch (error) {
            showError(error.response?.data?.message || error.message || 'Registration failed');
        }
    };

    return (
        <div></div>
    );




}
export default SpecialRegistration;