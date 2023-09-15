import {useSnackbar} from "notistack";
import {useState} from "react";
import axios from "axios";
import {AVERAGE_TIME_TO_METRO, UNIQUE_VIEW} from "../../../utils/api";
import {Button} from "antd";
import {SimpleResponseModal} from "../templates/simple-response-modal";
import {SimpleResponseListModal} from "../templates/simple-response-list-modal";

export function GetUniqueView(){
    const {enqueueSnackbar, closeSnackbar} = useSnackbar();

    const [modalVisible, setModalVisible] = useState(false)
    const [modalList, setModalList] = useState()

    const handelOpen = () => {
        axios.get(UNIQUE_VIEW)
            .then((response) => {
                const data = response.data
                console.log(data)
                setModalList(data)
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
                Список уникальных View
            </Button>
            <SimpleResponseListModal title={"Список уникальных View"}
                                 visible={modalVisible}
                                 list={modalList}
                                 handleOk={handleModalOk}
            />
        </>
    )
}