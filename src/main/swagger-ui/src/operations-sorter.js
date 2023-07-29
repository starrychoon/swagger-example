const methodsOrder = [
  'get',
  'post',
  'put',
  'patch',
  'delete',
  'options',
  'trace',
]

const PathAndMethodOperationsSorter = (a, b) => {
  const compare = a.get('path').localeCompare(b.get('path'), 'en', {numeric: true, sensitivity: 'base'})
  if (compare !== 0) return compare

  return methodsOrder.indexOf(a.get('method')) - methodsOrder.indexOf(b.get('method'))
}

export default PathAndMethodOperationsSorter
