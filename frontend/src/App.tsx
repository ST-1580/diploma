import React from "react";
import "./App.css";
import Alpha from "./Alpha";
import Beta from "./Beta";
import Gamma from "./Gamma";
import Delta from "./Delta";
import Space from "./Space";

import {BrowserRouter, Routes, Route, Link } from "react-router-dom";

function App() {
  return (
    <BrowserRouter>
      <Header />
      <Routes>
        <Route path="/" Component={ Space }></Route>
        <Route path="alpha" Component={ Alpha }></Route>
        <Route path="/beta" Component={ Beta }></Route>
        <Route path="/gamma" Component={ Gamma }></Route>
        <Route path="/delta" Component={ Delta }></Route>
      </Routes>
      <Footer />
    </BrowserRouter>
  );
}

function Header() {
  return (
    <header className="header">
      <Link to="/">Space</Link>
      <Link to="/alpha">Alpha</Link>
      <Link to="/beta">Beta</Link>
      <Link to="/gamma">Gamma</Link>
      <Link to="/delta">Delta</Link>
    </header>
  )
}

function Footer() {
  return (
    <footer>
        Made by st1580
    </footer>
  )
}

export default App;