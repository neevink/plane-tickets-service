import {Divider, Layout, Space, Typography} from "antd";
import {Content} from "antd/es/layout/layout";
import MyFooter from "../components/main/footer";
import MyHeader from "../components/main/header";
import {GetMostExpensive} from "../components/response/agency/get-most-expensive";
import {FindWithBalcony} from "../components/response/agency/find-with-balcony";

export function AgencyPage(){
    return (
        <>
            <Layout style={{minHeight: "100vh"}}>
                <MyHeader selectedMenuItem={'agency'}/>
                <Content>
                    <Space style={{
                        marginTop: 50,
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                        flexDirection: "column"
                    }}
                           size={0}
                    >
                        <Typography style={{marginBottom: 50, fontSize: "2vh", fontWeight: "bold"}}>Калькулятор для квартир</Typography>
                        <GetMostExpensive/>
                        <Divider style={{width: 800}}/>
                        <FindWithBalcony/>
                    </Space>
                </Content>
                <MyFooter/>
            </Layout>
        </>
    )
}