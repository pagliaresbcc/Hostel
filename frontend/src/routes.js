import React from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';

import Logon from './pages/Logon';
import Register from './pages/Register';
import Profile from './pages/Profile';
import NewReservation from './pages/NewReservation';
import Room from './pages/Room';
import NewRoom from './pages/NewRoom';
import SelectAvailableRooms from './pages/SelectAvailableRooms';
import SelectPayment from './pages/SelectPayment';
import UpdateReservation from './pages/UpdateReservation';
import UpdateAvailableRooms from './pages/UpdateAvailableRooms';

export default function Routes(){
    return(
        <BrowserRouter>
            <Switch>
                <Route path="/" exact component={Logon}/>
                <Route path="/register" exact component={Register}/>
                <Route path="/profile" exact component={Profile}/>
                <Route path="/reservation/newReservation" component={NewReservation}/>
                <Route path="/reservation/updateReservation" component={UpdateReservation}/>
                <Route path="/room" exact component={Room}/>
                <Route path="/room/newRoom" component={NewRoom}/>
                <Route path="/room/selectAvailableRooms" component={SelectAvailableRooms}/>
                <Route path="/room/updateAvailableRooms" component={UpdateAvailableRooms}/>
                <Route path="/payment/selectPayment" component={SelectPayment}/>
            </Switch>
        </BrowserRouter>
    );
}