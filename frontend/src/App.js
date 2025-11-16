import { BrowserRouter, Routes } from "react-router-dom";
import Navbar from "./components/common/Navbar";


function App(){

  return(
    <BrowserRouter>
      <Navbar/>

      <div className="content">
        <Routes>

        </Routes>
      </div>



    </BrowserRouter>

  )

}

export default App;