import React from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';

import Logon from './pages/Logon';
import Register from './pages/Register';
import Profile from './pages/Profile';
import ProfileAdmin from './pages/ProfileAdmin';
import Reservations from './pages/Reservations';
import NewReservation from './pages/NewReservation';
import UpdateReservation from './pages/UpdateReservation';
import Rooms from './pages/Rooms';
import NewRoom from './pages/NewRoom';
import SelectAvailableRooms from './pages/SelectAvailableRooms';
import UpdateSelectAvailableRooms from './pages/UpdateSelectAvailableRooms';
import SelectPayment from './pages/SelectPayment';
import UpdateSelectPayment from './pages/UpdateSelectPayment';

export default function Routes(){
    return(
        <BrowserRouter>
            <Switch>
                <Route path="/" exact component={Logon}/>
                <Route path="/register" exact component={Register}/>
                <Route path="/profile" exact component={Profile}/>
                <Route path="/profileAdmin" exact component={ProfileAdmin}/>
                <Route path="/reservations" exact component={Reservations}/>
                <Route path="/reservations/newReservation" component={NewReservation}/>
                <Route path="/reservations/updateReservation" component={UpdateReservation}/>
                <Route path="/rooms" exact component={Rooms}/>
                <Route path="/rooms/newRoom" component={NewRoom}/>
                <Route path="/rooms/selectAvailableRooms" component={SelectAvailableRooms}/>
                <Route path="/rooms/updateSelectAvailableRooms" component={UpdateSelectAvailableRooms}/>
                <Route path="/payments/selectPayment" component={SelectPayment}/>
                <Route path="/payments/updateSelectPayment" component={UpdateSelectPayment}/>
            </Switch>
        </BrowserRouter>
    );
}