import {Layout, Menu, Switch} from "antd";
import {CalculatorOutlined, TableOutlined} from "@ant-design/icons";
import {Link} from "react-router-dom";
import {useThemeSwitcher} from "react-css-theme-switcher";
import {useState} from "react";

const {Header} = Layout

export default function MyHeader({selectedMenuItem}){
    const [isDarkMode, setIsDarkMode] = useState();
    const { switcher, currentTheme, themes} = useThemeSwitcher();

    const rightStyle = {position: 'absolute', top: 0, right: 0}

    function toggleTheme(isChecked) { // added
        setIsDarkMode(isChecked);
        switcher({ theme: isChecked ? themes.dark : themes.light });
    }

    const items = [
        {
            icon: <TableOutlined/>,
            label: (
                <Link to={"/catalog"}>
                    Catalog
                </Link>
            ),
            key: "catalog"
        },
        {
            icon: <CalculatorOutlined/>,
            label: (
                <Link to={"/agency"}>
                    Agency
                </Link>
            ),
            key: "agency"
        },
    ];

    return (
        <>
            <Header>
                <Menu
                    theme={"dark"}
                    mode={"horizontal"}
                    items={items}
                    defaultSelectedKeys={[selectedMenuItem]}
                />
                <Menu selectable={false} theme={"dark"} mode='horizontal' style={rightStyle}>
                    <Menu.Item>
                        <Switch style={{width: 20}}
                            checkedChildren="🌜"
                            unCheckedChildren="🌞"
                            checked={isDarkMode || currentTheme === "dark"}
                            onChange={toggleTheme}
                        />
                    </Menu.Item>
                </Menu>
            </Header>
        </>
    )
}