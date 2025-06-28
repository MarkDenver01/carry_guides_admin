export default function Dashboard() {
    return (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 p-4">
            <div className="p-4 bg-white rounded-lg shadow">📊 Total Users: 1,200</div>
            <div className="p-4 bg-white rounded-lg shadow">💰 Revenue: $45,000</div>
            <div className="p-4 bg-white rounded-lg shadow">🚀 New Signups: 120</div>
        </div>
    );
}
