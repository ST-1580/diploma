import React from "react";
import "./App.css";
import Alpha from "./alpha/Alpha";
import Beta from "./beta/Beta";
import Gamma from "./gamma/Gamma";
import Delta from "./delta/Delta";
import Space from "./space/Space";

import { BrowserRouter, Routes, Route, Link } from "react-router-dom";

function App() {
  return (
    <BrowserRouter>
      <Header />
      <Routes>
        <Route path="/" Component={Space}></Route>
        <Route path="alpha" Component={Alpha}></Route>
        <Route path="/beta" Component={Beta}></Route>
        <Route path="/gamma" Component={Gamma}></Route>
        <Route path="/delta" Component={Delta}></Route>
      </Routes>
      <Footer />
    </BrowserRouter>
  );
}

function Header() {
  return (
    <header>
      <div className="flex_header">
        <div className="header_left">
          <Link to="/">Space</Link>
        </div>
        <div className="header_right">
          <Link to="/alpha">Alpha</Link>
        </div>
        <div className="header_right">
          <Link to="/beta">Beta</Link>
        </div>
        <div className="header_right">
          <Link to="/gamma">Gamma</Link>
        </div>
        <div className="header_right">
          <Link to="/delta">Delta</Link>
        </div>
      </div>
    </header>
  )
}

function Footer() {
  return (
    <footer>
      <div>
        Made by st1580
      </div>
    </footer>
  )
}

export default App;