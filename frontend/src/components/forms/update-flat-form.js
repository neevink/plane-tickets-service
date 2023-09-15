import {useSnackbar} from "notistack";
import {useForm} from "antd/es/form/Form";
import {useState} from "react";
import axios from "axios";
import {FLATS_API} from "../../utils/api";
import {Button, Form} from "antd";
import {InputNumber} from "antd/es";
import FlatForm from "./flat-form";

export function UpdateFlatForm(){
    const {enqueueSnackbar, closeSnackbar} = useSnackbar();

    const [updateForm] = useForm();
    const [isUpdateFlatModalOpen, setIsUpdateFlatModalOpen] = useState(false);
    const [initialValues, setInitialValues] = useState(undefined);
    const [flatId, setFlatId] = useState(undefined);

    const showUpdateFlatModal = (e) => {
        axios.get(`${FLATS_API}/${e["id"]}`)
            .then((response) => {
                let data = undefined
                if (response.data){
                    data = response.data
                }
                if (data === undefined){
                    enqueueSnackbar("Квартира не найдена!", {
                        autoHideDuration: 2000,
                        variant: "error"
                    })
                }
                setInitialValues(data)
                setIsUpdateFlatModalOpen(true)
                setFlatId(e["id"])
            })
            .catch((err) => {
                let error = err.response.data
                enqueueSnackbar(error.message, {
                    autoHideDuration: 5000,
                    variant: "error"
                })
            })
    }

    const handleFormSubmit = (body) => {
        if (flatId) {
            axios.put(`${FLATS_API}/${flatId}`, body)
                .then((response) => {
                    const newFlat = response.data
                    enqueueSnackbar("Успешно обновлена квартира с id: " + newFlat.id, {
                        autoHideDuration: 5000,
                        variant: "success"
                    })
                    setIsUpdateFlatModalOpen(false)
                })
        }
    }

    const handleUpdateFlatCancel = () => {
        setIsUpdateFlatModalOpen(false)
    }

    return (
        <>
            <Form form={updateForm}
                  onFinish={showUpdateFlatModal}
                  labelCol={{span: 8}}
                  wrapperCol={{span: 16}}
                  layout={"inline"}
            >
                <Form.Item label={"ID"}
                           name={"id"}
                           rules={[
                               {required: true, message: "Пожалуйста введите ID!"},
                               () => ({
                                   validator(_, value) {
                                       if (Number.isInteger(Number(value)) && value > 0) {
                                           return Promise.resolve();
                                       }
                                       return Promise.reject(new Error('ID должен быть больше 0!'));
                                   },
                               }),
                           ]}
                           style={{width: 200}}
                >
                    <InputNumber/>
                </Form.Item>
                <Form.Item>
                    <Button type={"primary"} onClick={updateForm.submit} style={{width: 150}}>
                        Обновить квартиру
                    </Button>
                </Form.Item>
            </Form>
            <FlatForm formVisible={isUpdateFlatModalOpen && initialValues !== undefined}
                      onCancel={handleUpdateFlatCancel}
                      onFinish={handleFormSubmit}
                      title={initialValues ? `Обновить квартиру с ID: ${flatId}` : "Добавить квартиру"}
                      initialValues={initialValues}
            />
        </>
    )
}