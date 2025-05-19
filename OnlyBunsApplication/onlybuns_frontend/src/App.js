import "./App.css";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Pocetna from "./pages/Pocetna";
import Prijava from "./pages/Prijava";
import Registracija from "./pages/Registracija";
import PrijavljeniKorisnikPregled from "./pages/PrijavljeniKorisnikPregled";
import AdminSistemView from "./pages/AdminSistemView";
import NovaObjava from "./pages/NovaObjava";
import ObjavaPrikaz from './pages/ObjavaPrikaz';
import ProfilKorisnika from './pages/ProfilKorisnika';
import React from 'react';
import BookedTrainings from "./pages/BookedTrainings";

function App() {
  return (
    <div className="App">
      <Router>
        <Routes>
          <Route path="/" element={<Pocetna />} />
          <Route path="/prijava" element={<Prijava />} />
          <Route path="/registracija" element={<Registracija />} />
          <Route path="/prijavljeniKorisnikPregled" element={<PrijavljeniKorisnikPregled/>} />
          <Route path="/adminSistemView" element={<AdminSistemView/>} />
          <Route path="/novaObjava" element={<NovaObjava/>} />
          <Route path="/objavaPrikaz" element={<ObjavaPrikaz />} />
          <Route path="/profilKorisnika" element={<ProfilKorisnika />} />
          <Route path="/bookedTrainings" element={<BookedTrainings />} />
        </Routes>
      </Router>
    </div>
  );
}

export default App;
