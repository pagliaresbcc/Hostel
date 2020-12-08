import React from 'react';
import { BrowserRouter, Route, Switch, Redirect } from 'react-router-dom';
import { isAuthenticated, isAdmin } from './auth';

import Login from './pages/Login';
import Register from './pages/Register';
import AdminProfile from './pages/Admin/AdminProfile';
import Users from './pages/Admin/Users';
import NewUser from './pages/Admin/NewUser';
import UpdateUser from './pages/Admin/UpdateUser';
import Reservations from './pages/Admin/Reservations';
import Rooms from './pages/Admin/Rooms';
import NewRoom from './pages/Admin/NewRoom';
import GuestProfile from './pages/Guest/GuestProfile';
import UpdateGuest from './pages/Guest/Update';
import NewReservation from './pages/Guest/NewReservation';
import UpdateReservation from './pages/Guest/UpdateReservation';
import SelectAvailableRooms from './pages/Guest/SelectAvailableRooms';
import UpdateSelectAvailableRooms from './pages/Guest/UpdateSelectAvailableRooms';
import SelectPayment from './pages/Guest/SelectPayment';
import UpdateSelectPayment from './pages/Guest/UpdateSelectPayment';

const AdminRoute = ({component: Component, ...rest}) => (
    < Route { ...rest} render={props => (
        isAuthenticated() ? (
            isAdmin() ? (
                <Component { ...props} />
            ) : (
                <Redirect to={{ pathname: '/guest/profile', state: { from: props.location}}} />
            )
        ) : (
            <Redirect to={{ pathname: '/', state: { from: props.location}}} />
        )
    )} />
)

const PrivateRoute = ({component: Component, ...rest}) => (
    < Route { ...rest} render={props => (
        isAuthenticated() ? (
            <Component { ...props} />
        ) : (
            <Redirect to={{ pathname: '/', state: { from: props.location}}} />
        )
    )} />
)

const Routes = () => (
    <BrowserRouter>
        <Switch>
            <Route path="/" exact component={Login}/>
            <Route path="/register" exact component={Register}/>
            <AdminRoute path="/admin/profile" exact component={AdminProfile}/>
            <AdminRoute path="/admin/guests" exact component={Users}/>
            <AdminRoute path="/admin/guests/new-guest" exact component={NewUser}/>
            <AdminRoute path="/admin/guests/update-guest" exact component={UpdateUser}/>
            <AdminRoute path="/admin/reservations" exact component={Reservations}/>
            <AdminRoute path="/admin/rooms" exact component={Rooms}/>
            <AdminRoute path="/admin/rooms/new-room" component={NewRoom}/>
            <PrivateRoute path="/guest/profile" exact component={GuestProfile}/>
            <PrivateRoute path="/guest/update" exact component={UpdateGuest}/>
            <PrivateRoute path="/guest/new-reservation" component={NewReservation}/>
            <PrivateRoute path="/guest/update-reservation" component={UpdateReservation}/>
            <PrivateRoute path="/guest/select-available-rooms" component={SelectAvailableRooms}/>
            <PrivateRoute path="/guest/update-selected-rooms" component={UpdateSelectAvailableRooms}/>
            <PrivateRoute path="/guest/select-payment" component={SelectPayment}/>
            <PrivateRoute path="/guest/update-selected-payment" component={UpdateSelectPayment}/>
        </Switch>
    </BrowserRouter>
)


export default Routes;