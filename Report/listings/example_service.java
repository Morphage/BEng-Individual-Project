Service<Message> fetchPerformanceStatsService = new Service<Message>() {
    @Override
    protected Task<Message> createTask() {
        ArrayList<String> payload = new ArrayList<String>();
        payload.add(myLoginName);
        Message requestMessage 
            = new Message(MessageCode.PERFORMANCE_STATS, payload);

        return new RequestServerTask(HOST, PORT, requestMessage);
    }
};
fetchPerformanceStatsService.restart();
