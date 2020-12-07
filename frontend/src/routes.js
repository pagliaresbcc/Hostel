import React from 'react';
import { BrowserRouter, Route, Switch, Redirect } from 'react-router-dom';
import { isAuthenticated, isAdmin } from './auth';

import Logon from './pages/Logon';
import Register from './pages/Register';
import ProfileAdmin from './pages/ProfileAdmin';
import ProfileUser from './pages/ProfileUser';
import Guest from './pages/Guest';
import NewGuest from './pages/NewGuest';
import UpdateGuest from './pages/UpdateGuest';
import UpdateUser from './pages/UpdateUser';
import Reservation from './pages/Reservation';
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
                <Redirect to={{ pathname: '/guests/profile', state: { from: props.location}}} />
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
            <AdminRoute path="/admin/profileAdmin" exact component={ProfileAdmin}/>
            <AdminRoute path="/admin/guests" exact component={Guest}/>
            <AdminRoute path="/admin/guests/newguest" exact component={NewGuest}/>
            <AdminRoute path="/admin/guests/updateguest" exact component={UpdateGuest}/>
            <PrivateRoute path="/guests/updateguest" exact component={UpdateUser}/>
            <PrivateRoute path="/guests/profile" exact component={ProfileUser}/>
            <PrivateRoute path="/reservations" exact component={Reservation}/>
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