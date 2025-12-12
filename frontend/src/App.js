import { BrowserRouter, Routes, Route } from "react-router-dom";
import Navbar from "./components/common/Navbar";
import Footer from "./components/common/Footer";
import RegisterPage from "./components/auth/RegisterPage";
import LoginPage from "./components/auth/LoginPage";
import HomePage from "./components/pages/HomePage";
import FindFlightsPage from "./components/pages/FindFlightsPage";
import ProfilePage from "./components/profile/ProfilePage";
import UpdateProfilePage from "./components/profile/UpdateProfile";
import BookingPage from "./components/pages/BookingPage";

function App(){

  return(
    <BrowserRouter>
      <Navbar/>

      <div className="content">
        <Routes>
          {/* AUTH PAGE */}
          <Route path="/register" element={<RegisterPage/>}/>
          <Route path="/login" element={<LoginPage/>}/>
          <Route path="/profile" element={<ProfilePage/>}/>
          <Route path="/update-profile" element={<UpdateProfilePage/>}/>
          <Route path="/book-flight/:id" element={<BookingPage />}/>

          {/* PUBLIC PAGES */}
          <Route path="/home" element={<HomePage />} />
          <Route path="/flights" element={<FindFlightsPage />} />

        </Routes>
      </div>


      <Footer/>
    </BrowserRouter>

  )

}

export default App;