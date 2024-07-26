// Report.js

const mongoose = require('mongoose');

const ReportSchema = new mongoose.Schema({
    content: {
        type: Object,
        required: true
    },
    created_at: {
        type: Date,
        default: Date.now
    }
});

module.exports = mongoose.model('Report', ReportSchema);