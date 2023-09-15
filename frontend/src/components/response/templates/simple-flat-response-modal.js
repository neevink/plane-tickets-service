import {Button, Descriptions, Modal, Typography} from "antd";


export function SimpleFlatResponseModal({title, value, visible, handleOk}){
    const getDescription = (label, value) => {
        return (
            value ? <Descriptions.Item label={label}>{value}</Descriptions.Item> : <></>
        )
    }

    return (
        <>
            <Modal open={visible}
                   title={title}
                   onCancel={handleOk}
                   footer={[
                       <Button type={"primary"} key={"submit"} onClick={handleOk}>
                           Ок
                       </Button>
                   ]}
                   width={1000}
                   bordered
            >
                {
                    value ? <Descriptions title={"Информация о квартире"} layout={"inline"}>
                        {getDescription("ID", value?.id)}
                        {getDescription("Name", value?.name)}
                        {getDescription("Area", value?.area)}
                        {getDescription("Number of rooms", value?.numberOfRooms)}
                        {getDescription("Floor", value?.floor)}
                        {getDescription("Time to metro on foot", value?.timeToMetroOnFoot)}
                        {getDescription("Balcony", value?.balcony.toString())}
                        {getDescription("View", value?.view)}
                        {getDescription("Price", value?.price)}
                        {getDescription("Coordinates X", value?.coordinates?.x)}
                        {getDescription("Coordinates Y", value?.coordinates?.y)}
                        {getDescription("Home name", value?.house?.name)}
                        {getDescription("Home number of floors", value?.house?.numberOfFloors)}
                        {getDescription("Home year", value?.house?.year)}
                    </Descriptions> : <Typography>Нет ответа</Typography>
                }
            </Modal>
        </>
    )
}