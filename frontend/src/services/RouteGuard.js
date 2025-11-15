import { Navigate, useLocation } from "react-router-dom";//useLocation 是 “读取当前 URL 信息”
import ApiService from "./ApiService";

export const RouteGuard = ({ element: Component, allowedRoles }) => {
    const location = useLocation();

    let hasRequiredRole = false;

    if (!allowedRoles || allowedRoles.length === 0){
        hasRequiredRole == false;// Deny access if no roles are explicitly allowed
    } else {
        //调远程的 ApiService.isAdmin()/isPilot()/isCustomer() 判断当前用户是什么角色
        hasRequiredRole = allowedRoles.some(role => {

            //check id the user has the matching roles need to access the route
            if (role === 'ADMIN'){
                return ApiService.isAdmin();
            } else if (role === 'PILOT'){
                return ApiService.isPilot();
            }else if (role === 'CUSTOMER'){
                return ApiService.isCustomer();
            }

            return false;
        });
    }

    //If the user has the required role(s), render the Component
    if (hasRequiredRole){
        return Component;//element: Component：要渲染的真正页面组件（比如 <AdminPage />）
    } else {
        //If not authorized, redirect to the login page
        return <Navigate to="/login" replace state={{ from: location }} />;
    }


};