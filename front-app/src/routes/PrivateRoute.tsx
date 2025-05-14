import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";

interface PrivateRouteProps {
    authentication: boolean;
}

export default function PrivateRoute({ authentication }: PrivateRouteProps): React.ReactElement | null {
    const { isAuthenticated, loading } = useAuth();
    if (loading) return null;

    if ( authentication) {
        return isAuthenticated ? <Outlet /> : <Navigate to="/login" />;
    } else {
        return isAuthenticated ? <Navigate to='/' /> : <Outlet />;
    }
}