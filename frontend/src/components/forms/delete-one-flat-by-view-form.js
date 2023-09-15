import {useSnackbar} from "notistack";
import {useForm} from "antd/es/form/Form";
import axios from "axios";
import {DELETE_ONE_FLAT_BY_VIEW, FLATS_API} from "../../utils/api";
import {Button, Form, Input} from "antd";
import {InputNumber} from "antd/es";

export function DeleteOneFlatByViewForm(){
    const {enqueueSnackbar, closeSnackbar} = useSnackbar();
    const [deleteForm] = useForm();

    const handleFlatDeleteByView = (e) => {
        axios.delete(`${DELETE_ONE_FLAT_BY_VIEW}/${e["view"]}`)
            .then((response) => {
                    enqueueSnackbar(`Успешно удалена квартира с view ${e['view']}`, {
                        autoHideDuration: 5000,
                        variant: "success"
                    })
                }
            )
            .catch((err) => {
                let error = err.response.data
                enqueueSnackbar(error.message, {
                    autoHideDuration: 5000,
                    variant: "error"
                })
            })
    }

    return (
        <>
            <Form form={deleteForm}
                  onFinish={handleFlatDeleteByView}
                  layout={"inline"}
                  labelCol={{span: 8}}
                  wrapperCol={{span: 16}}
            >
                <Form.Item label={"View"}
                           name={"view"}
                           rules={[
                               {required: true, message: 'Пожалуйста введите Вид!'},
                           ]}
                           style={{width: 200}}
                >
                    <Input/>
                </Form.Item>
                <Form.Item>
                    <Button type={"primary"} onClick={deleteForm.submit} style={{width: 150}}>
                        Удалить квартиру
                    </Button>
                </Form.Item>
            </Form>
        </>
    )
}