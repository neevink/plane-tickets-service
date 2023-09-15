import {useSnackbar} from "notistack";
import {useState} from "react";
import axios from "axios";
import {AVERAGE_TIME_TO_METRO} from "../../../utils/api";
import {Button} from "antd";
import {SimpleResponseModal} from "../templates/simple-response-modal";

export function GetAverageTimeToMetro(){
    const {enqueueSnackbar, closeSnackbar} = useSnackbar()

    const [modalVisible, setModalVisible] = useState(false)
    const [modalValue, setModalValue] = useState()

    const handelOpen = () => {
        axios.get(AVERAGE_TIME_TO_METRO)
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
            <Button type={"primary"} onClick={handelOpen}>
                Вычислить среднее время до метро пешком
            </Button>
            <SimpleResponseModal title={"Среднее время до метро пешком"}
                                 visible={modalVisible}
                                 value={modalValue}
                                 handleOk={handleModalOk}
            />
        </>
    )
}