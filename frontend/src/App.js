import { BrowserRouter, Routes } from "react-router-dom";
import Navbar from "./components/common/Navbar";
import Footer from "./components/common/Footer";


function App(){

  return(
    <BrowserRouter>
      <Navbar/>

      <div className="content">
        <Routes>

        </Routes>
      </div>


      <Footer/>
    </BrowserRouter>

  )

}

export default App;