import React from 'react';
import { BrowserRouter, Route, Switch, Redirect } from 'react-router-dom';
import { isAuthenticated, isAdmin } from './auth';

import Logon from './pages/Logon';
import Register from './pages/Register';
import Profile from './pages/Profile';
import ProfileAdmin from './pages/ProfileAdmin';
import ProfileUser from './pages/ProfileUser';
import Customers from './pages/Customers';
import NewCustomer from './pages/NewCustomer';
import UpdateCustomer from './pages/UpdateCustomer';
import UpdateUser from './pages/UpdateUser';
import Reservations from './pages/Reservations';
import NewReservation from './pages/NewReservation';
import UpdateReservation from './pages/UpdateReservation';
import Rooms from './pages/Rooms';
import NewRoom from './pages/NewRoom';
import SelectAvailableRooms from './pages/SelectAvailableRooms';
import UpdateSelectAvailableRooms from './pages/UpdateSelectAvailableRooms';
import SelectPayment from './pages/SelectPayment';
import UpdateSelectPayment from './pages/UpdateSelectPayment';

const AdminRoute = ({component: Component, ...rest}) => (
    < Route { ...rest} render={props => (
        isAuthenticated() ? (
            isAdmin() ? (
                <Component { ...props} />
            ) : (
                <Redirect to={{ pathname: '/profileUser', state: { from: props.location}}} />
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
            <Route path="/" exact component={Logon}/>
            <Route path="/register" exact component={Register}/>
            <AdminRoute path="/profileAdmin" exact component={ProfileAdmin}/>
            <PrivateRoute path="/profileUser" exact component={ProfileUser}/>
            <AdminRoute path="/customers" exact component={Customers}/>
            <AdminRoute path="/customers/newCustomer" exact component={NewCustomer}/>
            <AdminRoute path="/customers/updateCustomer" exact component={UpdateCustomer}/>
            <PrivateRoute path="/user/updateCustomer" exact component={UpdateUser}/>
            <PrivateRoute path="/profile" exact component={Profile}/>
            <PrivateRoute path="/reservations" exact component={Reservations}/>
            <PrivateRoute path="/reservations/newReservation" component={NewReservation}/>
            <PrivateRoute path="/reservations/updateReservation" component={UpdateReservation}/>
            <AdminRoute path="/rooms" exact component={Rooms}/>
            <AdminRoute path="/rooms/newRoom" component={NewRoom}/>
            <PrivateRoute path="/rooms/selectAvailableRooms" component={SelectAvailableRooms}/>
            <PrivateRoute path="/rooms/updateSelectAvailableRooms" component={UpdateSelectAvailableRooms}/>
            <PrivateRoute path="/payments/selectPayment" component={SelectPayment}/>
            <PrivateRoute path="/payments/updateSelectPayment" component={UpdateSelectPayment}/>
        </Switch>
    </BrowserRouter>
)


export default Routes;