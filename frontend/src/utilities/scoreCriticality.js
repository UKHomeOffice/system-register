function scoreCriticality(criticality) {
    switch (criticality) {
        case 'cni':
            return 4
        case 'high':
            return 3
        case 'medium':
            return 2
        case 'low':
            return 1
        default:
            return 1
    }
}

export default scoreCriticality