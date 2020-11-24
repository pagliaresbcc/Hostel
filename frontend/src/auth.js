export const isAuthenticated = () => {
    if(sessionStorage.getItem("token") === null) {
        return false;
    }
    return true;
};

export const isAdmin = () => {
    if(sessionStorage.getItem("role") === 'ROLE_ADMIN' ) {
        return true;
    }
    return false;
};