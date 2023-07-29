const CaseInsensitiveTagFilterPlugin = system => ({
  fn: {
    opsFilter: (taggedOps, phrase) => {
      // case insensitive match
      return taggedOps.filter((val, key) => key.toLowerCase().includes(phrase.toLowerCase()))
    },
  },
})

export default CaseInsensitiveTagFilterPlugin
