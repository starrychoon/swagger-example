import React from 'react'

const CustomPlugin = (system) => {
  return {
    components: {
      Logo: () => (
        <img src='https://logoproject.naver.com/img/img_story_renewal.png' alt='naver' height='40'/>
      )
    },
    fn: {
      opsFilter: (taggedOps, phrase) => {
        // case-insensitive match
        return taggedOps.filter((val, key) => key.toLowerCase().includes(phrase.toLowerCase()))
      }
    }
  }
}

export default CustomPlugin
