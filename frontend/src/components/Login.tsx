import React, { useState, type FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import ReCAPTCHA from 'react-google-recaptcha';
import api from '../libs/api';

const Login: React.FC = () => {
    const [email, setEmail] = useState<string>('');
    const [password, setPassword] = useState<string>('');
    const [recaptchaValue, setRecaptchaValue] = useState<string | null>(null);
    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string>('');
    const navigate = useNavigate();

    const handleLogin = async (e: FormEvent) => {
        e.preventDefault();

        if (!recaptchaValue) {
            setError('Please complete the reCAPTCHA');
            return;
        }

        try {
            setLoading(true);
            setError('');

            // Fetch CSRF token
            await api.get('/csrf_token');

            const response = await api.post('/auth/public/user_login', {
                email,
                password,
            });

            console.log('Login success:', response.data);
            // TODO: store auth token, etc.
            navigate('/dashboard');
        } catch (err) {
            console.error(err);
            setError('Login failed. Please check credentials.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="bg-white w-[95%] max-w-[400px] rounded-lg shadow-2xl p-8">
            <h2 className="text-2xl text-center text-blue-800 font-bold mb-2">Admin Portal</h2>
            <p className="text-center text-sm text-gray-600 mb-6">Enter your account</p>

            <form onSubmit={handleLogin} className="space-y-4">
                <div>
                    <label className="block text-sm font-medium text-gray-700">Email address</label>
                    <input
                        type="email"
                        className="mt-1 w-full px-4 py-2 border rounded shadow-sm focus:ring focus:ring-blue-300"
                        placeholder="Enter your email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700">Password</label>
                    <input
                        type="password"
                        className="mt-1 w-full px-4 py-2 border rounded shadow-sm focus:ring focus:ring-blue-300"
                        placeholder="Enter your password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>

                <div className="flex justify-center">
                    <ReCAPTCHA
                        sitekey="6LdiamErAAAAAKqiA3FrNCyMC_H2srGF1U0qebnV"
                        onChange={(value: string | null) => setRecaptchaValue(value)}
                    />
                </div>

                {error && <p className="text-sm text-red-600 text-center">{error}</p>}

                <button
                    type="submit"
                    disabled={loading}
                    className="w-full bg-blue-600 hover:bg-blue-700 text-white py-2 rounded transition duration-300"
                >
                    {loading ? 'Logging in...' : 'Login'}
                </button>
            </form>
        </div>
    );
};

export default Login;
