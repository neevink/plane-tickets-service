import {useSnackbar} from "notistack";
import {useState} from "react";
import {FLATS_API} from "../../utils/api";
import axios from "axios";
import {Button} from "antd";
import FlatForm from "./flat-form";

export function AddFlatForm(){
    const {enqueueSnackbar, closeSnackbar} = useSnackbar()
    const [isAddFlatModalOpen, setIsAddFlatModalOpen] = useState(false)
    const showModifyFlatModal = () => {setIsAddFlatModalOpen(true)}
    const handleAddFlatOk = () => {setIsAddFlatModalOpen(false)}
    const handleAddFlatCancel = () => {setIsAddFlatModalOpen(false)}

    const onFormSubmit = (e) => {
        axios.post(FLATS_API, e)
            .then((response) => {
                const newFlat = response.data
                enqueueSnackbar("Создана новая квартира с id: " + newFlat.id, {
                    autoHideDuration: 5000,
                    variant: "success"
                })
                handleAddFlatOk()
            })
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
            <Button type={"primary"} onClick={showModifyFlatModal} style={{width: 150, flex: 1}}>
                Добавить квартиру
            </Button>
            <FlatForm formVisible={isAddFlatModalOpen}
                      onCancel={handleAddFlatCancel}
                      onFinish={onFormSubmit}
                      title={"Добавить квартиру"}
            />
        </>
    )

}