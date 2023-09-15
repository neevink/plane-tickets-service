import {Layout} from "antd";

const {Footer} = Layout

export default function MyFooter(){
    return (
        <>
            <Footer
                style={{
                    textAlign: 'center',
                    width: '100%'
                }}
            >
                SOA lab 2 created by Egor Krivonosov P34111
            </Footer>
        </>
    )
}