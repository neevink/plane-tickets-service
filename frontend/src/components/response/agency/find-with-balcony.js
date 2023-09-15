import {useSnackbar} from "notistack";
import {useState} from "react";
import axios from "axios";
import {Button, Form, Radio, Switch} from "antd";
import {InputNumber} from "antd/es";
import {useForm} from "antd/es/form/Form";
import {SimpleFlatResponseModal} from "../templates/simple-flat-response-modal";
import {FIND_WITH_BALCONY, GET_MOST_EXPENSIVE} from "../../../utils/api";
import {CheckOutlined, CloseOutlined} from "@ant-design/icons";

export function FindWithBalcony(){
    const [form] = useForm()

    const {enqueueSnackbar, closeSnackbar} = useSnackbar()

    const [modalVisible, setModalVisible] = useState(false)
    const [modalValue, setModalValue] = useState()

    const handelOpen = (e) => {
        axios.get(`${FIND_WITH_BALCONY}/${e["cheapest"]}/${e["with-balcony"]}`)
            .then((response) => {
                const data = response.data
                setModalValue(data)
                setModalVisible(true)
            })
            .catch((err) => {
                let error = err.response.data
                enqueueSnackbar(error.message, {
                    autoHideDuration: 5000,
                    variant: "error"
                })
            })
    }

    const handleModalOk = () => {
        setModalVisible(false)
    }

    return (
        <>
            <Form form={form}
                  onFinish={handelOpen}
                  layout={"inline"}
                  labelCol={{span: 16}}
                  wrapperCol={{span: 8}}
            >
                <Form.Item label={"Cheapest"}
                           name={"cheapest"}
                           rules={[
                               {required: true, message: "Выберите дорогая ли квартира!"},
                           ]}
                           initialValue={false}
                >
                    <Switch style={{marginLeft: 8}}
                            checkedChildren={<CheckOutlined/>}
                            unCheckedChildren={<CloseOutlined/>}
                            defaultChecked={false}
                    />
                </Form.Item>
                <Form.Item label={"With balcony"}
                           name={"with-balcony"}
                           rules={[
                               {required: true, message: "Выберите если ли балкон у квартиры!"},
                           ]}
                           initialValue={false}
                >
                    <Switch style={{marginLeft: 8}}
                            checkedChildren={<CheckOutlined/>}
                            unCheckedChildren={<CloseOutlined/>}
                            defaultChecked={false}
                    />
                </Form.Item>
                <Form.Item>
                    <Button type={"primary"} onClick={form.submit} style={{width: 200}}>
                        Вычислить результат
                    </Button>
                </Form.Item>
            </Form>
            <SimpleFlatResponseModal title={"Полученный результат"}
                                     visible={modalVisible}
                                     value={modalValue}
                                     handleOk={handleModalOk}
            />
        </>
    )
}