import {ko} from 'date-fns/esm/locale'
import React from "react"
import DatePicker from "react-datepicker"
import "react-datepicker/dist/react-datepicker.css"

const JsonSchema_string_date = (props) => {
  const dateNumber = Date.parse(props.value);
  const date = dateNumber ? new Date(dateNumber) : new Date();
  return (
    <DatePicker
      selected={date}
      locale={ko}
      onChange={d => props.onChange(d.toISOString().substring(0, 10))}
      dateFormat='yyyy-MM-dd'
      dateFormatCalender='yyyy년 MM월'
      showYearDropdown
      showMonthDropdown
    />
  )
}

const JsonSchema_string_date_time = (props) => {
  const dateNumber = Date.parse(props.value);
  const date = dateNumber ? new Date(dateNumber) : new Date();
  return (
    <DatePicker
      selected={date}
      locale={ko}
      onChange={d => props.onChange(d.toISOString())}
      dateFormat='yyyy-MM-dd HH:mm:ss'
      dateFormatCalender='yyyy년 MM월'
      showYearDropdown
      showMonthDropdown
      showTimeSelect
      timeIntervals={1}
    />
  )
}

const DateTimeSwaggerPlugin = system => ({
  components: {
    JsonSchema_string_date: JsonSchema_string_date,
    "JsonSchema_string_date-time": JsonSchema_string_date_time
  }
})

export default DateTimeSwaggerPlugin
