import {useSnackbar} from "notistack";
import {useState} from "react";
import axios from "axios";
import {Button, Form} from "antd";
import {InputNumber} from "antd/es";
import {useForm} from "antd/es/form/Form";
import {SimpleFlatResponseModal} from "../templates/simple-flat-response-modal";
import {GET_MOST_EXPENSIVE} from "../../../utils/api";
import {SimpleResponseModal} from "../templates/simple-response-modal";

export function GetMostExpensive(){
    const [form] = useForm()

    const {enqueueSnackbar, closeSnackbar} = useSnackbar()

    const [modalVisible, setModalVisible] = useState(false)
    const [modalValue, setModalValue] = useState()

    const handelOpen = (e) => {
        axios.get(`${GET_MOST_EXPENSIVE}/${e["id1"]}/${e["id2"]}/${e["id3"]}`)
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
                  labelCol={{span: 8}}
                  wrapperCol={{span: 8}}
            >
                <Form.Item label={"ID1"}
                           name={"id1"}
                           rules={[
                               {required: true, message: "Пожалуйста введите ID1!"},
                               () => ({
                                   validator(_, value) {
                                       if (Number.isInteger(Number(value)) && value > 0) {
                                           return Promise.resolve();
                                       }
                                       return Promise.reject(new Error('ID1 должен быть больше 0!'));
                                   },
                               }),
                           ]}
                           style={{width: 200}}
                >
                    <InputNumber/>
                </Form.Item>
                <Form.Item label={"ID2"}
                           name={"id2"}
                           rules={[
                               {required: true, message: "Пожалуйста введите ID2!"},
                               () => ({
                                   validator(_, value) {
                                       if (Number.isInteger(Number(value)) && value > 0) {
                                           return Promise.resolve();
                                       }
                                       return Promise.reject(new Error('ID2 должен быть больше 0!'));
                                   },
                               }),
                           ]}
                           style={{width: 200}}
                >
                    <InputNumber/>
                </Form.Item>
                <Form.Item label={"ID3"}
                           name={"id3"}
                           rules={[
                               {required: true, message: "Пожалуйста введите ID3!"},
                               () => ({
                                   validator(_, value) {
                                       if (Number.isInteger(Number(value)) && value > 0) {
                                           return Promise.resolve();
                                       }
                                       return Promise.reject(new Error('ID3 должен быть больше 0!'));
                                   },
                               }),
                           ]}
                           style={{width: 200}}
                >
                    <InputNumber/>
                </Form.Item>
                <Form.Item>
                    <Button type={"primary"} onClick={form.submit} style={{width: 200}}>
                        Выбрать самую дорогую
                    </Button>
                </Form.Item>
            </Form>
            <SimpleResponseModal title={"Самая дорогая квартира"}
                                     visible={modalVisible}
                                     value={modalValue}
                                     handleOk={handleModalOk}
            />
        </>
    )
}