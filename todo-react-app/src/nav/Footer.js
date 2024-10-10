import { Outlet } from "react-router-dom";
import BottomNavBar from "./BottomNavBar";

const Footer = () => {
  return (
    <>
      <Outlet />
      <div style={{ marginTop: 'auto', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
        <BottomNavBar />
      </div>
      
    </>
  );
}

export default Footer;
