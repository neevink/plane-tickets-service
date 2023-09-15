import './App.css';
import {SnackbarProvider} from "notistack";
import {Navigate, Route, Routes} from "react-router-dom";
import FlatsCatalogPage from "./pages/flats-catalog-page";
import {AgencyPage} from "./pages/agency-page";
import {ThemeSwitcherProvider} from "react-css-theme-switcher";

function App() {
    const currThemes = {
        dark: `${process.env.PUBLIC_URL}/antd.dark-theme.css`,
        light: `${process.env.PUBLIC_URL}/antd.light-theme.css`,
    };

    return (
        <ThemeSwitcherProvider themeMap={currThemes} defaultTheme="light">
            <SnackbarProvider maxSnack={3}>
                <Routes>
                    <Route path={"/catalog"} element={<FlatsCatalogPage/>}/>
                    <Route path={"/agency"} element={<AgencyPage/>}/>
                    <Route path={"*"} element={<Navigate to={"/catalog"} replace/>}/>
                </Routes>
            </SnackbarProvider>
        </ThemeSwitcherProvider>
    );
}

export default App;
