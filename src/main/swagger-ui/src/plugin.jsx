import React from 'react'

const CustomLogoPlugin = system => ({
  components: {
    Logo: () => (
      <img src='https://logoproject.naver.com/img/img_story_renewal.png' alt='naver' height='40'/>
    ),
  },
})

export default CustomLogoPlugin
